from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import tensorflow as tf
import numpy as np

app = FastAPI()

# 모델 로드
model = tf.keras.models.load_model('model/lstm_model.h5')

# 입력 데이터 스키마 정의
class HealthDataRequest(BaseModel):
    user_id: str
    heart_rate: list
    spo2: list

# 예측 함수
def predict_anomaly(heart_rate_data):
    # 데이터 shape 맞추기 (batch_size=1, timesteps, features)
    try:
        data = np.array(heart_rate_data).reshape(1, -1, 1)
    except:
        raise ValueError("데이터 shape 오류: heart_rate는 1차원 배열이어야 함.")
    
    reconstruction = model.predict(data)
    error = np.mean(np.abs(reconstruction - data))
    threshold = 0.05  # 예시값 (조정 가능)
    is_anomaly = error > threshold
    return error, is_anomaly

# API 엔드포인트
@app.post("/predict")
def predict(data: HealthDataRequest):
    try:
        error, alert = predict_anomaly(data.heart_rate)
        response = {
            "user_id": data.user_id,
            "anomaly_score": float(error),
            "alert": bool(alert),
            "recommendation": "휴식과 물 섭취를 권장합니다." if alert else "정상 상태로 보입니다."
        }
        return response
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
