# Лабораторная работа №9: Конфигурирование IPsec VPN (Вариант 3)

**Группа:** LR-23-JS
**Имя Фамилия:** Максим Иванов
**Дата:** 25.03.2026
**ВАРИАНТ:** 3

---

## 🛠️ Параметры сети (Вариант 3)

| Сеть | IP-адрес | Маска | Описание |
| :--- | :--- | :--- | :--- |
| **LAN 1** | 10.0.1.0 | /24 | Сеть за Router 0 |
| **LAN 2** | 10.0.2.0 | /24 | Сеть за Router 0 |
| **LAN 3** | 10.0.3.0 | /24 | Сеть за Router 1 |
| **LAN 4** | 10.0.4.0 | /24 | Сеть за Router 1 |
| **WAN 1** | 10.0.5.0 | /24 | Между Router 0 и Router ISP |
| **WAN 2** | 10.0.6.0 | /24 | Между Router 1 и Router ISP |

**Трафик туннеля:** LAN 2 ↔ LAN 3 (IPsec VPN)

---

## 🔑 Параметры VPN (Вариант 3)

*   **Authentication:** pre-share
*   **Encryption:** aes
*   **Hash:** md5
*   **Group:** Group 5 (Diffie-Hellman)
*   **Life-time:** 300
*   **Key:** cisco3
*   **Transform-set:** esp-aes

---

## 💻 Конфигурация устройств

### 1. Настройка хостов (IP & Gateway)
*   **PC0 (LAN 1):** IP 10.0.1.1 | GW 10.0.1.254
*   **PC1 (LAN 2):** IP 10.0.2.1 | GW 10.0.2.254
*   **PC2 (LAN 3):** IP 10.0.3.1 | GW 10.0.3.254
*   **PC3 (LAN 4):** IP 10.0.4.1 | GW 10.0.4.254

---

### 2. Router 0 (Левый) - Настройка интерфейсов и VPN

```ios
enable
conf t
! Интерфейсы
int fa0/0
 ip addr 10.0.1.254 255.255.255.0
 no shut
int fa0/1
 ip addr 10.0.2.254 255.255.255.0
 no shut
int s0/0/0
 ip addr 10.0.5.1 255.255.255.0
 no shut

! Статическая маршрутизация
ip route 10.0.3.0 255.255.255.0 10.0.5.2
ip route 10.0.4.0 255.255.255.0 10.0.5.2
ip route 10.0.6.0 255.255.255.0 10.0.5.2

! --- IPsec VPN Configuration ---
! 1. ISAKMP Policy (Phase 1)
crypto isakmp policy 10
 encryption aes
 hash md5
 authentication pre-share
 group 5
 lifetime 300
crypto isakmp key cisco3 address 10.0.6.1

! 2. Transform Set (Phase 2)
crypto ipsec transform-set vpn-set esp-aes

! 3. ACL для трафика туннеля (LAN 2 <-> LAN 3)
access-list 100 permit ip 10.0.2.0 0.0.0.255 10.0.3.0 0.0.0.255

! 4. Crypto Map
crypto map vpn-map 10 ipsec-isakmp
 set peer 10.0.6.1
 set transform-set vpn-set
 match address 100

! 5. Применение на внешний интерфейс
int s0/0/0
 crypto map vpn-map
```

---

### 3. Router 1 (Правый) - Настройка интерфейсов и VPN

```ios
enable
conf t
! Интерфейсы
int fa0/0
 ip addr 10.0.3.254 255.255.255.0
 no shut
int fa0/1
 ip addr 10.0.4.254 255.255.255.0
 no shut
int s0/0/0
 ip addr 10.0.6.1 255.255.255.0
 no shut

! Статическая маршрутизация
ip route 10.0.1.0 255.255.255.0 10.0.6.2
ip route 10.0.2.0 255.255.255.0 10.0.6.2
ip route 10.0.5.0 255.255.255.0 10.0.6.2

! --- IPsec VPN Configuration ---
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

int s0/0/0
 crypto map vpn-map
```

---

### 4. Router ISP (Центральный)

```ios
enable
conf t
int s0/0/0
 ip addr 10.0.5.2 255.255.255.0
 no shut
int s0/0/1
 ip addr 10.0.6.2 255.255.255.0
 no shut
! Провайдеру не нужны маршруты до LAN сетей в реальной жизни, 
! но для Packet Tracer можно добавить, чтобы пинг работал до VPN.
```

---

## ✅ Проверка
1.  **Пинг:** `PC1 (LAN 2) -> PC2 (LAN 3)` должен успешно проходить.
2.  **SA Status:** Введите `show crypto ipsec sa`. Вы должны увидеть количество зашифрованных/расшифрованных пакетов (`#pkts encaps`, `#pkts decaps`).
3.  **Simulation:** В режиме симуляции пакет между LAN 2 и LAN 3 будет иметь заголовок **ESP** — это значит, что он зашифрован.

---
**Имя файла:** `LR-23-JS-Maxim_Ivanov-Lab9_V3.pkt` 🦾
