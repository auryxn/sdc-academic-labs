# ЧЕК-ЛИСТ: Как собрать Лабораторную №8 за 15 минут (Вариант 3)

Сэр, вот ваш четкий план действий в Cisco Packet Tracer.

---

### **Шаг 1: Сборка топологии (нарисовать схему)**
1.  Возьмите **2 роутера 2911** (Router2 и Router3).
2.  Возьмите **3 коммутатора 2960** (Switch0, Switch1, Swil).
3.  Возьмите **2 ПК** (PC0, PC1) и **3 сервера** (Server0, Server1, Server2).
4.  Соедините их **черными прямыми кабелями** (Copper Straight-Through) точно как на вашей последней картинке:
    *   PC0 -> Switch0 (Fa0/2)
    *   PC1 -> Switch0 (Fa0/1)
    *   Switch0 -> Router2 (Gi0/0)
    *   Router2 -> Switch1 (Gi0/1)
    *   Server0/Server1 -> Switch1 (Fa0/2, Fa0/3)
    *   **Router2 -> Router3** (Gi0/2 -> Gi0/0) — *Здесь используйте перекрестный кабель (Copper Cross-Over), если индикатор красный.*
    *   Router3 -> Swil (Gi0/1)
    *   Swil -> Server2 (Fa0/2)
    *   Swil -> PC2/C2 (Fa0/1)

---

### **Шаг 2: Настройка хостов (IP-адреса)**
Кликните на каждое устройство, выберите **Desktop > IP Configuration**:

*   **PC0:** 192.168.28.1 | 255.255.255.0 | GW: 192.168.28.254
*   **PC1:** 192.168.28.2 | 255.255.255.0 | GW: 192.168.28.254
*   **Server0:** 10.9.28.1 | 255.255.255.0 | GW: 10.9.28.254
*   **Server1:** 10.9.28.2 | 255.255.255.0 | GW: 10.9.28.254
*   **Server2:** 68.160.0.1 | 255.240.0.0 | GW: 68.175.255.254

---

### **Шаг 3: Настройка роутеров (Копировать-Вставить)**
Откройте **CLI** роутера и вставьте команды (правой кнопкой мыши -> Paste):

**Router2 (Левый):**
```ios
enable
conf t
int gi0/0
 ip addr 192.168.28.254 255.255.255.0
 ip nat inside
 no shut
int gi0/1
 ip addr 10.9.28.254 255.255.255.0
 ip nat inside
 no shut
int gi0/2
 ip addr 183.6.9.14 255.255.255.248
 ip nat outside
 no shut
ip route 0.0.0.0 0.0.0.0 183.6.9.9
ip nat inside source static 10.9.28.1 183.6.9.10
ip nat inside source static 10.9.28.2 183.6.9.11
access-list 1 permit 192.168.28.0 0.0.0.255
ip nat inside source list 1 interface gi0/2 overload
end
write
```

**Router3 (Правый/ISP):**
```ios
enable
conf t
int gi0/0
 ip addr 183.6.9.9 255.255.255.248
 no shut
int gi0/1
 ip addr 68.175.255.254 255.240.0.0
 no shut
end
write
```

---

### **Шаг 4: Настройка Switch0 (Port Security)**
**CLI Switch0:**
```ios
enable
conf t
int fa0/1
 sw mode access
 sw port-security
 sw port-security mac-address sticky
 sw port-security violation shutdown
int fa0/2
 sw mode access
 sw port-security
 sw port-security violation restrict
end
write
```

---

### **Шаг 5: Проверка**
1.  Убедитесь, что все индикаторы (треугольники) **зеленые**.
2.  Пингуйте с **PC0** адрес сервера ISP: `68.160.0.1`. Пинг должен пройти!
3.  На **Router2** введите: `show ip nat translations`. Увидите записи о трансляции.

---
**Имя файла для сохранения:** `LR-23-JS-Maxim_Ivanov-Lab8_V3.pkt` 🦾
