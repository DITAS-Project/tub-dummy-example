#/bin/sh
COMPONENT_URL="http://keycloak:8000/v1/routes"
attempts=0
until $(curl --output /dev/null --silent $COMPONENT_URL); do
    if [ ${attempts} -eq 100 ]; then
      echo "Max attempts reached, cannot connect to component"
      exit 1
    fi

    printf '.'
    attempts=$(($attempts+1))
    sleep 9
done

./kcc --headless --address http://keycloak:8000 --bl blueprint.json --cf users.json
