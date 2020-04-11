# search-engine

Instrukcja

1.  Uruchomienie projektu należy rozpocząć od pobrania repozytorium https://github.com/mattizz/search-engine
2. Następnie korzystając z polecenia Gradle należy zbudować paczkę I wystartować aplikację pamiętając od tym aby port 8080 nie był zajęty

•	gradle build
•	gradle bootRun

3. Do aplikacji należy przejść poprzez adres URL: http://localhost:8080/ . W oknie powinna pojawić się aplikacja:
4. W sekcji “Upload” można uploadować zarówno pojedyncze pliki jak I zestawy plików.
5. Należy wybrać pliki do wgrania a następnie kliknąć przycisk Upload
   W odpowiedzi zwrócone zostaną informacje o dodanych plikach:
6. Następnie należy cofnąć się do strony głównej I w sekcji search wpisać słow, zawarte w dokumencie. (W przypadku gdy słowo nie występuje w dokumencie zwrócona zostaje pusta wartość).
   Klikająć przycisk search otrzymujemy wynik w postaci listy dokumentów zawierających wyszukiwane słowo posortowanych według TFIDF
7. W sekcji TF-IDF for word istnieje możliwość otrzymania konkretnych wartości TF-IDF dla danego słowa występującego w dokumentach
   Wynikiem będą wartości TFIDF
