FROM python:3.8.5-alpine3.12
COPY ./src /app
WORKDIR /app
RUN pip install -r /app/requirements.txt
ENTRYPOINT ["python"]
CMD ["./app.py"]