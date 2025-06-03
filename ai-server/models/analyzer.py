import numpy as np
from models.models import OverworkModel, HeartAbnormalModel, FallDetectionModel
import os

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

class MultiDetector:
    def __init__(self):
        self.overwork_model = OverworkModel(model_path=os.path.join(BASE_DIR, "training", "pretrained_models", "fatigue_detection_model.joblib"),
                                            scaler_path=os.path.join(BASE_DIR, "training", "pretrained_models", "fatigue_scaler.pkl"))
        self.heart_model = HeartAbnormalModel(model_path=os.path.join(BASE_DIR, "training", "pretrained_models","lstm_autoencoder_model.keras"),
                                              scaler_path=os.path.join(BASE_DIR, "training", "pretrained_models","heart_abnormal_scaler.pkl"),
                                              threshold_path=os.path.join(BASE_DIR, "training", "pretrained_models","best_threshold.npy"))
        self.fall_model = FallDetectionModel(model_path=os.path.join(BASE_DIR, "training", "pretrained_models","fall_detection_model.pt"),
                                             scaler_path=os.path.join(BASE_DIR, "training", "pretrained_models","fall_detection_scaler.pkl"))

    def detect_overwork(self, heart_rate, step_count, energy, speed):
        return self.overwork_model.predict(heart_rate, step_count, energy, speed)

    def detect_heart_abnormal(self, heart_rate, timestamp):
        return self.heart_model.predict(heart_rate, timestamp)

    def detect_fall(self, accel, gyro, heart_rate, timestamp):
        return self.fall_model.predict(accel, gyro, heart_rate, timestamp)

    def predict(self, data):
        overwork = self.detect_overwork(data.heartRate, data.stepCount, data.activeEnergyBurned, data.runningSpeed)
        heart_abnormal = self.detect_heart_abnormal(data.heartRate, data.timestamp)
        fall = self.detect_fall(data.accel, data.gyro, data.heartRate, data.timestamp)

        if fall:
            event = "낙상/충돌"
        elif heart_abnormal:
            event = "심박 이상"
        elif overwork:
            event = "과로"
        else:
            event = "정상"

        return {"userId": data.userId, "event": event}