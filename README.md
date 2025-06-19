# 🍭 CandyTimer

Minecraft sunucunuzda belirli saatlerde otomatik komut çalıştırmanızı sağlayan bir Bukkit/Spigot eklentisi.

---

## ✨ Özellikler

### 🎯 Temel Özellikler
- Hassas zamanlama - HH:mm formatında dakika bazında kontrol
- Çoklu komut desteği - Her timer için birden fazla komut
- YAML yapılandırması - Kolay düzenlenebilir timers.yml dosyası
- Duplicate prevention - Aynı timer günde bir kez çalışır

### ⚡ Performans Optimizasyonları
- Smart caching - Timer verilerini memory'de cache'ler
- Memory efficient - 100 timer için sadece ~25 KB
- Akıllı kontrol - Gereksiz işlemleri önler
- Auto cleanup - Gece yarısında otomatik temizlik
---

## 📦 Kurulum

1. Plugin dosyasını `plugins/` klasörüne koyun
2. Sunucuyu başlatın (timers.yml otomatik oluşturulur)
3. `plugins/CandyTimer/timers.yml` dosyasını düzenleyin
4. Sunucuyu yeniden başlatın veya reload yapın

---

## ⚙️ Yapılandırma

### Dosya Yapısı
```
plugins/
└── CandyTimer/
    └── timers.yml
```

### timers.yml Örneği
```yaml
timers:
  # Sabah mesajı
  sabah_mesaji:
    time: "09:00"
    commands:
      - "broadcast &eGünaydın herkese!"
 
  # Öğlen etkinliği
  oglen_etkinligi:
    time: "12:00"
    commands:
      - "broadcast &bÖğlen yemeği vakti"
      - "give @a bread 5"
       
  # Akşam ödülleri
  aksam_odulleri:
    time: "21:30"
    commands:
      - "broadcast &dGünlük ödüller dağıtılıyor!"
      - "give @a diamond 1"
      - "give @a emerald 2"
  
  # Gece yarısı temizlik
  gece_temizlik:
    time: "00:00"
    commands:
      - "broadcast &8Sunucu temizliği başlıyor..."
      - "lagg clear"
      - "save-all"
      - "broadcast &aTemizlik tamamlandı!"
  
  # Event saati
  event_zamani:
    time: "20:00"
    commands:
      - "broadcast &cEVENT BAŞLIYOR! Spawna gelin!"
      - "event start pvp"
```
---

## 📈 Performans

| Timer Sayısı | Memory Kullanımı | CPU Etkisi |
|--------------|------------------|------------|
| 10 Timer     | ~2.5 KB         | <%0.01     |
| 100 Timer    | ~25 KB          | <%0.1      |
| 1000 Timer   | ~250 KB         | <%1        |

Modern Minecraft sunucuları için bu değerler bence ihmal edilebilir seviyededir.

---

## ⚠️ Dikkat Edilmesi Gerekenler

### ✅ Doğru Kullanımlar
- Timer isimlerinde Türkçe karakter kullanmayın
- Komutlarda `/` koymayın (otomatik eklenir)
- Ağır komutları sunucunun peak saatlerinde çalıştırmayın

### ❌ Yanlış Kullanımlar
- Aynı anda çok fazla ağır komut
- Çok sık tekrarlanan timerlar (her dakika gibi)
- Server crash edebilecek riskli komutlar
---


### Memory Management
- Cache sistemi ile optimal performans
- Günlük executed set temizliği
- Memory leak prevention

---

## 📋 Sistem Gereksinimleri

- **Minecraft**: 1.19+
- **Platform**: Bukkit/Spigot/Paper
- **RAM**: Minimal (timer başına ~250 bytes)

---

## 📄 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.

---

🙏 Teşekkürler
---
*Not: Bu plugin öylesine yapılmıştır ama yinede bazılarının işine yarayabilir...*