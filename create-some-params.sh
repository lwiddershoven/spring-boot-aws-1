#!/bin/sh

# "inspired by" https://stackoverflow.com/questions/28830225/how-to-read-a-properties-file-which-contains-keys-that-have-a-period-character
# because the region and param path must be the same as the app expects we'll just read in the app config.
file="src/main/resources/application.properties"

if [ -f "$file" ]
then
  echo "$file found."

  while IFS='=' read -r key value
  do
    key=$(echo $key | tr '.' '_')

    if [ -n "${key}" ]
    then
      eval ${key}=\${value}
    fi

  done < "$file"

  echo "Using AWS Region     : " ${config_aws_ssm_region}
  echo "Using Parameter Path : " ${config_aws_ssm_path}
else
  echo "$file not found."
fi

if [ -z "${config_aws_ssm_region}" ]
then
    echo "Properties file did not contain expected region config"
    return 1
fi

if [ -z "${config_aws_ssm_path}" ]
then
    echo "Properties file did not contain expected parameter path config"
    return 2
fi

# See for docs: https://docs.aws.amazon.com/cli/latest/reference/ssm/index.html

echo "Saving ${config_aws_ssm_path}/test-string"
aws ssm put-parameter \
    --region "${config_aws_ssm_region}" \
    --name "${config_aws_ssm_path}/test-string" \
    --value "test-value" \
    --type String

echo "Saving ${config_aws_ssm_path}/test-password"
aws ssm put-parameter \
    --region "${config_aws_ssm_region}" \
    --name "${config_aws_ssm_path}/test-password" \
    --value "p#sW*rd33" \
    --type SecureString

