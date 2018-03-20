# Veri İndirgeme
Bu proje Kocaeli Üniversitesi 3.Sınıf Öğrencilerine 2017-2018 eğitim döneminde Yazılım Laboratuvarı dersinde ödev olarak verilmiştir. Projenin server kısmı IntelliJ IDEA, client kısmı ise Android Studio kullanırak geliştirilmiştir. 

 --> [Münir KARSLI](https://github.com/munirKarsli/)

 --> [Yusuf SATILMIŞ](https://github.com/satilmisyusuf/)


### Server
![alt text](https://github.com/satilmisyusuf/veri-indirgeme/blob/master/images/gezinge2.png)

Projede iki adet server bulunmaktadır. 
 
- Birincisinde androidden gelen json formatındaki veriler quadtree ile indexlendikten sonra indirgeme işlemi yapılmaktadır. İndirgeme işleminden sonra veriler tekrar json formatına çevrilerek androide cevap olarak gönderilmektedir.

- İkincisinde ise yine androidden gelen json formatındaki veriler quadtree ile indexlendikten sonra arama işlemi yapılmaktadır. Arama işleminden sonra veriler tekrar json formatına çevrilerek androide cevap olarak gönderilmektedir.

### Client
![alt text](https://github.com/satilmisyusuf/veri-indirgeme/blob/master/images/gezinge.gif)

Bu bölüm android uygulaması olarak geliştirilmiştir. 

Kullanıcı bağlantı için gerekli bilgileri girdikten sonra istediği bir veri dosyasını seçebilmektedir. Sonrasında ise orjinal lat-long bilgileri mavi ile, indirgeme sonrası serverdan veriler ise kırmızı renk ile belirtilmiştir. 

Arama işlemi için seekbardan yarıçap seçtikten sonra harita üstünde herhangi bi yere dokunup aranmak istenen alanı seçmesi gereklidir. Arama işleminden sonra daire içinde kalan orjinal lat-long noktaları sarı ile indirgenmiş veriye ait noktalar ise yeşil ile gösterilmiştir.


![alt text](https://github.com/satilmisyusuf/veri-indirgeme/blob/master/images/listview_res.png) ![alt text](https://github.com/satilmisyusuf/veri-indirgeme/blob/master/images/indirgeme_res.png)![alt text](https://github.com/satilmisyusuf/veri-indirgeme/blob/master/images/arama1_res.png)![alt text](https://github.com/satilmisyusuf/veri-indirgeme/blob/master/images/arama2_res.png)


