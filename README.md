# Mikroserwis służący do zarządzania wydarzeniami i biletami.

Run command: java -jar mikroserwis-0.0.1.jar --server.port=8181

Zwracany status code:

200 - wszystko OK \
401 - problem z tokenem\
400 - inne błędy

Zwracane są kody błędu:

100 - OK\
101 - nie ma wydarzenia z takim ID\
102 - token stracił ważność\
103 - nieprawidłowy podpis tokena\
104 - brak uprawnien - nie admin\
105 - nie ma biletu z takim ID\
106 - wydarzenie odwołane\
107 - czas rezygnacji minął\
108 - użytkownik nie ma żadnych wydarzeń\
109 - bilet był już zarezerwowany\
110 - bilet był już wolny\
111 - bilet niedostępny (anulowany)\
112 - niepoprawny token\
113 - problem z połączeniem z innym mkroserwisem\
114 - brak tokena