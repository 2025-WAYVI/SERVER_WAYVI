import joblib
from sklearn.preprocessing import StandardScaler
from sklearn.cluster import KMeans
import numpy as np
import pandas as pd
import os

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

class HealthReportModel:
    def __init__(self, scaler_path=None, cluster_model_path=None, pca_model_path=None):
        if cluster_model_path is None:
            cluster_model_path = os.path.join(BASE_DIR, "training", "pretrained_models", "kmeans_model.pkl")
        if pca_model_path is None:
            pca_model_path = os.path.join(BASE_DIR, "training", "pretrained_models", "pca_model.pkl")
        if scaler_path is None:
            scaler_path = os.path.join(BASE_DIR, "training", "pretrained_models", "health_scaler.pkl")
        
        self.cluster_model = joblib.load(cluster_model_path)
        self.pca_model = joblib.load(pca_model_path)
        self.scaler = joblib.load(scaler_path) if os.path.exists(scaler_path) else None

        self.model = None
        self.scaler_fit = None

    def fit(self, df, cols):
        X = df[cols]
        self.scaler = StandardScaler()
        X_scaled = self.scaler.fit_transform(X)

        self.model = KMeans(n_clusters=4, random_state=42)
        df['cluster'] = self.model.fit_predict(X_scaled)
        return df

    def predict(self, X):
        # predict시에는 fit된 scaler 또는 로드된 scaler 사용
        scaler_to_use = self.scaler_fit if self.scaler_fit is not None else self.scaler

        if scaler_to_use:
            X_scaled = scaler_to_use.transform(X)
        else:
            X_scaled = X
        cluster_labels = self.cluster_model.predict(X_scaled)
        return cluster_labels

    def transform_pca(self, X):
        scaler_to_use = self.scaler_fit if self.scaler_fit is not None else self.scaler

        if scaler_to_use:
            X_scaled = scaler_to_use.transform(X)
        else:
            X_scaled = X
        return self.pca_model.transform(X_scaled)


