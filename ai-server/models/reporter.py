import os
import numpy as np
import pandas as pd
from models.health_report import HealthReportModel
import json

BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))


class HealthReporter:
    cluster_summary_map = {
        0: "건강유지형",
        1: "저활동형",
        2: "과로주의형",
        3: "위험신호형"
    }

    def __init__(self):
        self.prev_data = {
            "stepCount": 6000,
            "runningSpeed": 6.0,
            "averageHeartRate": 70,
            "activeEnergyBurned": 600
        }

        self.health_report_model = HealthReportModel(
            scaler_path=os.path.join(BASE_DIR, "training", "pretrained_models", "health_scaler.pkl"),
            cluster_model_path=os.path.join(BASE_DIR, "training", "pretrained_models", "kmeans_model.pkl"),
            pca_model_path=os.path.join(BASE_DIR, "training", "pretrained_models", "pca_model.pkl"),
        )

    def compute_change(self, key, current_value):
        prev_value = self.prev_data.get(key)
        if prev_value is None:
            return 0  
        return ((current_value - prev_value) / prev_value) * 100

    def generate_warnings(self, current_data):
        warnings = []

        avg_hr = np.mean(current_data.get("heartRate", [0]))
        min_hr = np.min(current_data.get("heartRate", [0]))
        max_hr = np.max(current_data.get("heartRate", [0]))
        avg_resp = np.mean(current_data.get("respiratoryRate", [0]))
        avg_oxygen = np.mean(current_data.get("oxygenSaturation", [0]))
        avg_temp = np.mean(current_data.get("bodyTemperature", [0]))

        if max_hr > 100:
            warnings.append("빈맥")
        if min_hr < 50:
            warnings.append("서맥")
        if max_hr - min_hr > 40:
            warnings.append("부정맥")
        if avg_resp < 12 or avg_resp > 20:
            warnings.append("호흡기 질환")
        if avg_oxygen < 90:
            warnings.append("저산소증")
        if avg_temp < 36.0:
            warnings.append("저체온")
        elif avg_temp > 37.5:
            warnings.append("발열")

        return warnings

    def generate_report(self, user_id, current_data):

        features = {
            'Steps': current_data.get("stepCount", 0),
            'Calories_Burned': current_data.get("activeEnergyBurned", 0),
            'Heart_Rate': int(np.mean(current_data.get("heartRate", [0]))),
            'Blood_Oxygen_Level': int(np.mean(current_data.get("oxygenSaturation", [0]))),
            'Skin_Temperature': round(float(np.mean(current_data.get("bodyTemperature", [0]))), 1)
        }

        features_df = pd.DataFrame([features])
        cluster_label = self.health_report_model.predict(features_df)[0]
        summary = self.cluster_summary_map.get(cluster_label, "알 수 없음")

        avg_heart_rate = int(np.mean(current_data.get("heartRate", [0])))
        avg_oxygen = int(np.mean(current_data.get("oxygenSaturation", [0])))
        avg_resp_rate = int(np.mean(current_data.get("respiratoryRate", [0])))
        avg_body_temp = round(float(np.mean(current_data.get("bodyTemperature", [0]))), 1)
        running_speed_value = current_data.get("runningSpeed", 0)
        if isinstance(running_speed_value, list):
            avg_running_speed = round(float(np.mean(running_speed_value)), 1)
        else:
            avg_running_speed = round(float(running_speed_value), 1)


        step_count_change = self.compute_change("stepCount", current_data.get("stepCount", 0))
        running_speed_change = self.compute_change("runningSpeed", avg_running_speed)
        heart_rate_change = self.compute_change("averageHeartRate", avg_heart_rate)
        active_energy_change = self.compute_change("activeEnergyBurned", current_data.get("activeEnergyBurned", 0))

        warnings = self.generate_warnings(current_data)

        report = {
            "userId": user_id,
            "summary": summary,
            "stepCount": current_data.get("stepCount", 0),
            "stepCountChange": step_count_change,
            "averageRunningSpeed": avg_running_speed,
            "runningSpeedChange": running_speed_change,
            "averageHeartRate": avg_heart_rate,
            "heartRateChange": heart_rate_change,
            "averageOxygenSaturation": avg_oxygen,
            "averageRespiratoryRate": avg_resp_rate,
            "averageBodyTemperature": avg_body_temp,
            "activeEnergyBurned": current_data.get("activeEnergyBurned", 0),
            "activeEnergyChange": active_energy_change,
            "warning": warnings
        }

        return report