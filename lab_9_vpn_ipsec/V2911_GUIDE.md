# Лабораторная работа №9: Настройка IPsec VPN (Вариант 3)
## План на роутерах 2911

Сэр, отличный выбор. Роутеры 2911 имеют 3 порта GigabitEthernet, что позволит нам подключить всё без лишних модулей.

---

### **1. Топология и кабели (Router 2911)**
Используйте кабель **Copper Straight-Through** (сплошная черная линия) для всего!

*   **PC0** -> Router0 (**GigabitEthernet0/0**)
*   **PC1** -> Router0 (**GigabitEthernet0/1**)
*   **Router0 (Gi0/2)** -> **ISP (Gi0/0)**
*   **Router1 (Gi0/2)** -> **ISP (Gi0/1)**
*   **Router1 (Gi0/0)** <- **PC2**
*   **Router1 (Gi0/1)** <- **PC3**

---

### **2. Настройка левого роутера (Router0)**
Введите эти команды в CLI:

```ios
enable
conf t
! Интерфейсы к компам
int gi0/0
 ip addr 10.0.1.254 255.255.255.0
 no shut
int gi0/1
 ip addr 10.0.2.254 255.255.255.0
 no shut
! Интерфейс к ISP
int gi0/2
 ip addr 10.0.5.1 255.255.255.0
 no shut
! Маршрутизация
ip route 0.0.0.0 0.0.0.0 10.0.5.2
! VPN (Вариант 3)
crypto isakmp policy 10
 encryption aes
 hash md5
 authentication pre-share
 group 5
 lifetime 300
crypto isakmp key cisco3 address 10.0.6.1
crypto ipsec transform-set vpn-set esp-aes
access-list 100 permit ip 10.0.2.0 0.0.0.255 10.0.3.0 0.0.0.255
crypto map vpn-map 10 ipsec-isakmp
 set peer 10.0.6.1
 set transform-set vpn-set
 match address 100
int gi0/2
 crypto map vpn-map
exit
```

---

### **3. Настройка правого роутера (Router1)**
Введите эти команды в CLI:

```ios
enable
conf t
int gi0/0
 ip addr 10.0.3.254 255.255.255.0
 no shut
int gi0/1
 ip addr 10.0.4.254 255.255.255.0
 no shut
int gi0/2
 ip addr 10.0.6.1 255.255.255.0
 no shut
ip route 0.0.0.0 0.0.0.0 10.0.6.2
! VPN
crypto isakmp policy 10
 encryption aes
 hash md5
 authentication pre-share
 group 5
 lifetime 300
crypto isakmp key cisco3 address 10.0.5.1
crypto ipsec transform-set vpn-set esp-aes
access-list 100 permit ip 10.0.3.0 0.0.0.255 10.0.2.0 0.0.0.255
crypto map vpn-map 10 ipsec-isakmp
 set peer 10.0.5.1
 set transform-set vpn-set
 match address 100
int gi0/2
 crypto map vpn-map
exit
```

---

### **4. Настройка центрального роутера (ISP)**

```ios
enable
conf t
int gi0/0
 ip addr 10.0.5.2 255.255.255.0
 no shut
int gi0/1
 ip addr 10.0.6.2 255.255.255.0
 no shut
! Для тестов в Packet Tracer пропишем маршруты к LAN
ip route 10.0.1.0 255.255.255.0 10.0.5.1
ip route 10.0.2.0 255.255.255.0 10.0.5.1
ip route 10.0.3.0 255.255.255.0 10.0.6.1
ip route 10.0.4.0 255.255.255.0 10.0.6.1
exit
```

---

### **5. Главное условие ПИНГА:**
На **PC1** шлюз (Gateway) должен быть **10.0.2.254**.
На **PC2** шлюз (Gateway) должен быть **10.0.3.254**.

Пингуйте с **PC1** на **10.0.3.1**. Пакеты **ОБЯЗАНЫ** пойти! 🦾
