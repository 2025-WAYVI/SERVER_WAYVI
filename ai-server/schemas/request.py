from pydantic import BaseModel
from typing import List

# 이상 탐지 요청 스키마
class AnalyzeRequest(BaseModel):
    userId: str
    timestamp: str
    accel: List[List[float]]
    gyro: List[List[float]]
    heartRate: List[float]
    stepCount: float
    activeEnergyBurned: float
    runningSpeed: List[float]

# 건강 리포트 요청 스키마
class ReportRequest(BaseModel):
    userId: str
    date: str
    stepCount: float
    runningSpeed: float
    basalEnergyBurned: float
    activeEnergyBurned: float
    heartRate: List[float]
    oxygenSaturation: List[float]
    respiratoryRate: List[float]
    bodyTemperature: List[float]
