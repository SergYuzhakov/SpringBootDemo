### curl samples:
>for Windows Use GitBash

### list ToDo:
curl -s http://localhost:8080/api/todo

### create new ToDo with new description:
curl -s -i -X POST -d '{"description":"New Description 1"}' -H 'Content-Type:application/json' http://localhost:8080/api/todo

### update current ToDo with id:b9866bf5-3cfc-47fd-a6f0-5d31d9a59ea7":
curl -i -X PUT -H "Content-Type: application/json" -d '{"description":"Take the dog and the cat for a walk","id":"b9866bf5-3cfc-47fd-a6f0-5d31d9a59ea7""}' http://localhost:8080/api/todo

### check ToDo like completed with id:b9866bf5-3cfc-47fd-a6f0-5d31d9a59ea7:
curl -i -X PATCH http://localhost:8080/api/todo/b9866bf5-3cfc-47fd-a6f0-5d31d9a59ea7

### delete completed ToDo with id:b9866bf5-3cfc-47fd-a6f0-5d31d9a59ea7:
curl -i -X DELETE http://localhost:8080/api/todo/b9866bf5-3cfc-47fd-a6f0-5d31d9a59ea7

### Validation data
curl -i -X POST -H "Content-Type: application/json" -d '{"description":""}' http://localhost:8080/api/todo