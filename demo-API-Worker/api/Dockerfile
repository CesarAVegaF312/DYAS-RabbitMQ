FROM python:3.11-slim

ENV PYTHONUNBUFFERED=1

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 8000
# OJO: nuestro archivo se llama api.py, así que usamos api:app
CMD ["uvicorn", "api:app", "--host", "0.0.0.0", "--port", "8000"]
