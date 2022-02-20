### curl samples:
>for Windows Use GitBash

### List ToDo:
curl -s http://localhost:8080/api/todo

### Create new ToDo with new description:
curl -s -i -X POST -d '{"description":"New Description 1"}' -H 'Content-Type:application/json' http://localhost:8080/api/todo

### Update current ToDo with id:1000:
curl -i -X PUT -H "Content-Type: application/json" -d '{"description":"Take the dog and the cat for a walk","id":"1000"}' http://localhost:8080/api/todo

### Check ToDo like completed with id:1000:
curl -i -X PATCH http://localhost:8080/api/todo/1000

### Delete completed ToDo with id:1000:
curl -i -X DELETE http://localhost:8080/api/todo/

### Validation data
curl -i -X POST -H "Content-Type: application/json" -d '{"description":""}' http://localhost:8080/api/todo