# springboot-gcp-pubsub-examples
Springboot Apps that demonstrate interaction with Google Cloud PubSub and not using Springboot Integration but direct APIs

In order to run the emulator you must have a GCP account where you will need to setup the Cloud SDK as per the following link: https://cloud.google.com/deployment-manager/docs/step-by-step-guide/installation-and-setup

Once you have installed Cloud SDK, follow instructions to install the emulator: https://cloud.google.com/pubsub/docs/emulator

As per requirement, you must download the Python-docs-Sample: https://github.com/GoogleCloudPlatform/python-docs-samples.

You will also need to install Python 3.6 if you are running Windows 10 and also install PIP from the following location:https://pip.pypa.io/en/stable/installing/

Once you have everything setup, follow the emulator doc by running the following:
- gcloud components install pubsub-emulator
- gcloud components update
- gcloud beta emulators pubsub start --project=PUBSUB_PROJECT_ID [options]
- gcloud beta emulators pubsub env-init
- Set the following environments in Windows:
    - set PUBSUB_EMULATOR_HOST=localhost:8432
    - set PUBSUB_PROJECT_ID=my-project-id
- pip install -r requirements.txt (this is installed from the directory "python-docs-samples\pubsub\cloud-client"
- py publisher.py PUBSUB_PROJECT_ID create TOPIC_ID
- py subscriber.py PUBSUB_PROJECT_ID create TOPIC_ID SUBSCRIPTION_ID
- py publisher.py PUBSUB_PROJECT_ID publish TOPIC_ID
- py subscriber.py PUBSUB_PROJECT_ID receive SUBSCRIPTION_ID

The code has been revised based on the following Google implementation examples: https://github.com/googleapis/google-cloud-java/tree/master/google-cloud-examples/src/main/java/com/google/cloud/examples/pubsub/snippets
