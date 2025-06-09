from fastapi import FastAPI
from schemas.request import AnalyzeRequest, ReportRequest
from schemas.response import AnalyzeResponse, ReportResponse

app = FastAPI(title="AI 건강 모니터링 API", version="1.0.0")

# 이상 탐지 API
@app.post("/analyze", response_model=AnalyzeResponse)
def analyze(data: AnalyzeRequest):
    # 임시 응답
    return {
        "userId": data.userId,
        "event": "낙상/충돌" 
    }

# 건강 리포트 생성 API
@app.post("/report", response_model=ReportResponse)
def report(data: ReportRequest):
    # 임시 응답
    return {
        "userId": data.userId,
        "summary": "건강유지형",
        "stepCount": data.stepCount,
        "stepCountChange": -12,
        "averageRunningSpeed": data.runningSpeed,
        "runningSpeedChange": 0.8,
        "averageHeartRate": sum(data.heartRate) / len(data.heartRate),
        "heartRateChange": 11,
        "averageOxygenSaturation": sum(data.oxygenSaturation) / len(data.oxygenSaturation),
        "averageRespiratoryRate": sum(data.respiratoryRate) / len(data.respiratoryRate),
        "averageBodyTemperature": sum(data.bodyTemperature) / len(data.bodyTemperature),
        "activeEnergyBurned": data.activeEnergyBurned,
        "activeEnergyChange": -15,
        "warning": ["부정맥", "저체온"]
    }

# 엔드포인트 테스트용 
@app.get("/")
def root():
    return {"message": "AI Server is running!"}
