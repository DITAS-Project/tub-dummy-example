#!  /bin/sh
while getopts ":a:" opt; do
  case $opt in
    a) BASE_URL="$OPTARG"
    ;;

    \?) echo "Invalid option -$OPTARG" >&2
    ;;
  esac
done
echo "using $BASE_URL "
COMPONENT_URL="$BASE_URL/v1/routes"
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

./kcc --headless --address "$BASE_URL" --bl blueprint.json --cf users.json
