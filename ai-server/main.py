import os
from fastapi import FastAPI
from schemas.request import AnalyzeRequest, ReportRequest
from schemas.response import AnalyzeResponse, ReportResponse
from models.analyzer import MultiDetector
from models.reporter import HealthReporter

app = FastAPI()

BASE_DIR = os.path.dirname(os.path.abspath(__file__))

detector = MultiDetector()
reporter = HealthReporter()

@app.post("/analyze", response_model=AnalyzeResponse)
async def analyze(data: AnalyzeRequest):

    event_result = detector.predict(data)
    event = event_result.get("event", "정상")

    report = reporter.generate_report(
        user_id=data.userId,
        current_data=data.dict()
    )

    return AnalyzeResponse(userId=data.userId, event=event)
    
@app.post("/report", response_model=ReportResponse)
async def report(data: ReportRequest):
    report = reporter.generate_report(
        user_id=data.userId,
        current_data=data.dict()
    )
    return report