import json
import os
import logging

import boto3

s3 = boto3.client("s3")

BUCKET_NAME = os.environ["BUCKET_NAME"]


def lambda_handler(event, context):

    try:
        body = json.loads(event["body"])

        timestamp = body["timestamp"]

        key = f"sms/drop/{timestamp}.json"

        s3.put_object(
            Bucket=BUCKET_NAME,
            Key=key,
            Body=json.dumps(body),
            ContentType="application/json",
        )

        logging.info(f"File uploaded successfully to {key}")

        return {
            "statusCode": 200,
            "body": json.dumps(
                {
                    "success": True,
                    "s3_key": key,
                }
            ),
        }

    except Exception as e:
        logging.exception("Error processing request")

        return {
            "statusCode": 500,
            "body": json.dumps(
                {
                    "success": False,
                    "error": str(e),
                }
            ),
        }