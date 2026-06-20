import json
import os
import time
import hmac
import hashlib
import logging

import boto3

s3 = boto3.client("s3")

BUCKET_NAME = os.environ["BUCKET_NAME"]
HMAC_SECRET = os.environ["HMAC_SECRET"]


def generate_signature(
    sender: str,
    body: str,
    timestamp: int,
) -> str:

    canonical = (
        f"{sender}|{body}|{timestamp}"
    )

    return hmac.new(
        HMAC_SECRET.encode(),
        canonical.encode(),
        hashlib.sha256,
    ).hexdigest()


def lambda_handler(event, context):

    try:

        payload = json.loads(event["body"])

        sender = payload["sender"]
        message = payload["body"]
        timestamp = payload["timestamp"]
        signature = payload["signature"]

        now_ms = int(time.time() * 1000)

        if abs(now_ms - timestamp) > 300_000:
            return {
                "statusCode": 403,
                "body": json.dumps(
                    {
                        "success": False,
                        "error": "Request expired",
                    }
                ),
            }

        expected_signature = generate_signature(
            sender=sender,
            body=message,
            timestamp=timestamp,
        )

        if not hmac.compare_digest(
            signature,
            expected_signature,
        ):
            return {
                "statusCode": 403,
                "body": json.dumps(
                    {
                        "success": False,
                        "error": "Invalid signature",
                    }
                ),
            }

        key = f"sms/drop/{timestamp}.json"

        s3.put_object(
            Bucket=BUCKET_NAME,
            Key=key,
            Body=json.dumps(payload),
            ContentType="application/json",
        )

        logging.info(
            f"File uploaded successfully to {key}"
        )

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
        logging.exception(
            "Error processing request"
        )

        return {
            "statusCode": 500,
            "body": json.dumps(
                {
                    "success": False,
                    "error": str(e),
                }
            ),
        }