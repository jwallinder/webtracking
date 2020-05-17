echo "all visits"
curl -s http://localhost:8080/allVisits | jq
echo
echo "summary"
curl -s http://localhost:8080/summary | jq

