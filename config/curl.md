### curl samples:
>for Windows Use GitBash

### list ToDo:
curl -s http://localhost:8080/api/todo

### create new ToDo with new description:
curl -s -i -X POST -d '{"description":"New Description 1"}' -H 'Content-Type:application/json' http://localhost:8080/api/todo

### update current ToDo with id:5755a6ce-6616-4932-9da9-7dd4a3b916c7:
curl -i -X PUT -H "Content-Type: application/json" -d '{"description":"Take the dog and the cat for a walk","id":"5755a6ce-6616-4932-9da9-7dd4a3b916c7"}' http://localhost:8080/api/todo

### check ToDo like completed with id:5755a6ce-6616-4932-9da9-7dd4a3b916c7:
curl -i -X PATCH http://localhost:8080/api/todo/5755a6ce-6616-4932-9da9-7dd4a3b916c7

### delete completed ToDo with id:5755a6ce-6616-4932-9da9-7dd4a3b916c7:
curl -i -X DELETE http://localhost:8080/api/todo/5755a6ce-6616-4932-9da9-7dd4a3b916c7

### Validation data
curl -i -X POST -H "Content-Type: application/json" -d '{"description":""}' http://localhost:8080/api/todo