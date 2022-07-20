REST Api  Чата c комнатами

Реализован Spring Boot

Модели Person. Role. Room, Message.

URI :
/login - Post -аутентификация и авторизация

/role/ - Post - создание роли, Get - список всех ролей
/role/name/{name}/ - Get - поиск роли по имени
/role/id/{id}/ - Get - поиск роли по ID

/person/ - Get - список всех Person, Post - создание(регистрация) нового Person, Delete - удаление Person
/person/{id}/ - Get - поиск Person по ID
/person/{pId}/updRole/{rId}/ - Put - изменение Роли для Person

/room/ - Get - список всех комнат
/room/{pId}/ - Post - создание комнаты с Person ID
/room/{rId}/addPersonId/{pId}/ - Post - добавление в комнату по ID Person по ID
/room/{rId}/delPersonId/{pId}/ - Post - удаление из комнату по ID Person по ID
/room/name/{name}/ - Get - поиск комнаты по имени
/room/id/{id}/ - Get - поиск комнаты по ID

/message/roomId/{rId}/personId/{pId}/ - Post - создание нового сообщения в комнате по ID от Person по ID
/message/{mId}/personId/{pId}/ - Delete - удаление сообщения по ID Person ID