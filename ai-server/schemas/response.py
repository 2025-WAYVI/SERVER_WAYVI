from pydantic import BaseModel
from typing import List, Optional

# 이상 탐지 응답 스키마
class AnalyzeResponse(BaseModel):
    userId: str
    event: str  

# 건강 리포트 응답 스키마
class ReportResponse(BaseModel):
    userId: str
    summary: str 
    stepCount: float
    stepCountChange: float
    averageRunningSpeed: float
    runningSpeedChange: float
    averageHeartRate: float
    heartRateChange: float
    averageOxygenSaturation: float
    averageRespiratoryRate: float
    averageBodyTemperature: float
    activeEnergyBurned: float
    activeEnergyChange: float
    warning: Optional[List[str]]
