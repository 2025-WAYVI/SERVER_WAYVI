import os
import joblib
import numpy as np
import pandas as pd
import tensorflow as tf
import torch
import torch.nn as nn


BASE_DIR = os.path.dirname(os.path.abspath(__file__))

class OverworkModel:
    def __init__(self, model_path=None, scaler_path=None):
        if model_path is None:
            model_path = os.path.join(BASE_DIR, "..", "training", "pretrained_models", "fatigue_detection_model.joblib")
            model_path = os.path.normpath(model_path)
        self.model = joblib.load(model_path)
        
        if scaler_path is None:
            scaler_path = os.path.join(BASE_DIR, "..", "training", "pretrained_models", "fatigue_scaler.pkl")
            scaler_path = os.path.normpath(scaler_path)
        self.scaler = joblib.load(scaler_path)

    def predict(self, heart_rate, step_count, active_energy, running_speed):
        avg_hr = np.mean(heart_rate)
        avg_speed = np.mean(running_speed)
        features = np.array([avg_hr, step_count, active_energy, avg_speed]).reshape(1, -1)
        features_scaled = self.scaler.transform(features)
        pred = self.model.predict(features_scaled)
        return bool(pred[0])

class HeartAbnormalModel:
    def __init__(self, model_path=None, scaler_path=None, threshold_path=None):
        if model_path is None:
            model_path = os.path.join(BASE_DIR, "..", "training", "pretrained_models", "lstm_autoencoder_model.keras")
            model_path = os.path.normpath(model_path)
        self.model = tf.keras.models.load_model(model_path)

        if scaler_path is None:
            scaler_path = os.path.join(BASE_DIR, "..", "training", "pretrained_models", "heart_abnormal_scaler.pkl")
            scaler_path = os.path.normpath(scaler_path)
        self.scaler = joblib.load(scaler_path)
        
        if threshold_path is None:
            threshold_path = os.path.join(BASE_DIR, "..", "training", "pretrained_models", "best_threshold.npy")
            threshold_path = os.path.normpath(threshold_path)
        self.threshold = np.load(threshold_path)


    def predict(self, heart_rate, timestamp=None):
        
        hr_array = np.array(heart_rate).reshape(-1, 1)  # (T, 1)
        hr_scaled = self.scaler.transform(hr_array)      # 스케일링
        input_seq = hr_scaled[np.newaxis, :, :]          # (1, T, 1) 형태로 변환
        reconstructed = self.model.predict(input_seq)    # 재구성
        mse = np.mean((input_seq - reconstructed) ** 2) # MSE 계산
        is_abnormal = mse > self.threshold

        return is_abnormal

class FallDetectionNet(nn.Module):
    def __init__(self, input_dim=7, seq_len=5, hidden_dim=128, dropout=0.3):
        super().__init__()
        self.cnn = nn.Sequential(
            nn.Conv1d(in_channels=input_dim, out_channels=64, kernel_size=3, padding=1),
            nn.ReLU(),
            nn.BatchNorm1d(64),
            nn.Conv1d(in_channels=64, out_channels=128, kernel_size=3, padding=1),
            nn.ReLU(),
            nn.BatchNorm1d(128),
            nn.AdaptiveMaxPool1d(output_size=1),
            nn.Dropout(dropout)
        )
        self.lstm = nn.LSTM(
            input_size=128,
            hidden_size=hidden_dim,
            num_layers=1,
            batch_first=True,
            bidirectional=True
        )
        self.fc = nn.Sequential(
            nn.Dropout(dropout),
            nn.Linear(hidden_dim * 2, 1)
        )

    def forward(self, x):
        # x shape: (batch, seq_len, input_dim)
        x = x.transpose(1, 2)  # -> (batch, input_dim, seq_len)
        x = self.cnn(x)        # -> (batch, 128, seq_len//2)
        x = x.transpose(1, 2)  # -> (batch, seq_len//2, 128)
        _, (h_n, _) = self.lstm(x)  # h_n: (num_layers*2, batch, hidden_dim)
        h_n = h_n.view(1, 2, x.size(0), -1)  # (num_layers, num_directions, batch, hidden_dim)
        h_n = h_n[-1]  # (2, batch, hidden_dim)
        h_n = torch.cat((h_n[0], h_n[1]), dim=1)  # (batch, hidden_dim*2)
        out = self.fc(h_n)  # (batch, 1)
        return out.squeeze(1)  # 로짓 출력

class FallDetectionModel:
    def __init__(self, model_path=None, scaler_path=None, device=None):
        if model_path is None:
            model_path = os.path.join(BASE_DIR, "..", "training", "pretrained_models", "fall_detection_model.pt")
            model_path = os.path.normpath(model_path)

        self.device = device if device else torch.device("cuda" if torch.cuda.is_available() else "cpu")

        self.model = FallDetectionNet(input_dim=7, seq_len=5, hidden_dim=128, dropout=0.3)
        self.model.load_state_dict(torch.load(model_path, map_location=self.device))
        self.model.to(self.device)
        self.model.eval()

        if scaler_path is None:
            scaler_path = os.path.join(BASE_DIR, "..", "training", "pretrained_models", "fall_detection_scaler.pkl")
            scaler_path = os.path.normpath(scaler_path)
        self.scaler = joblib.load(scaler_path)

    def predict(self, accel, gyro, heart_rate, timestamp=None):
        accel = np.array(accel)
        gyro = np.array(gyro)
        heart_rate = np.array(heart_rate)

        # 7개 피처 (가속도 평균 3 + 자이로 평균 3 + 심박 평균 1)
        features = []
        features.extend(np.mean(accel, axis=0))
        features.extend(np.mean(gyro, axis=0))
        features.append(np.mean(heart_rate))

        features = np.array(features).reshape(1, 1, -1)  # (batch=1, seq_len=1, input_dim=7)
        features_scaled = self.scaler.transform(features.reshape(1, -1)).reshape(1, 1, -1)

        x = torch.tensor(features_scaled, dtype=torch.float32).to(self.device)

        with torch.no_grad():
            logits = self.model(x)
            prob = torch.sigmoid(logits).item()
            pred = prob > 0.5

        return bool(pred)
