# Инструкция по настройке Лабораторной работы №8 (Вариант 3)

**Сэр, это пошаговое руководство для настройки каждого устройства в Cisco Packet Tracer по Вашему 3-му варианту.**

---

## 1. Настройка компьютеров и серверов (Hosts)

Зайдите в каждое устройство: **Desktop > IP Configuration**.

| Устройство | IP-адрес | Маска подсети | Основной шлюз (Gateway) |
| :--- | :--- | :--- | :--- |
| **PC1 (NET A)** | 192.168.28.1 | 255.255.255.0 | 192.168.28.254 |
| **PC2 (NET A)** | 192.168.28.2 | 255.255.255.0 | 192.168.28.254 |
| **Server1 (NET B)** | 10.9.28.1 | 255.255.255.0 | 10.9.28.254 |
| **Server2 (NET B)** | 10.9.28.2 | 255.255.255.0 | 10.9.28.254 |
| **Server ISP (NET D)** | 68.160.0.1 | 255.240.0.0 | 68.175.255.254 |

---

## 2. Настройка маршрутизатора NAT (Router NAT)

Зайдите в **CLI** роутера и введите команды по очереди:

```ios
enable
configure terminal

! 1. Настройка внутреннего интерфейса (NET A)
interface Gig0/0
 ip address 192.168.28.254 255.255.255.0
 ip nat inside
 no shutdown

! 2. Настройка интерфейса для серверов (NET B)
interface Gig0/1
 ip address 10.9.28.254 255.255.255.0
 ip nat inside
 no shutdown

! 3. Настройка внешнего интерфейса (Сеть C)
interface Gig0/2
 ip address 183.6.9.14 255.255.255.248
 ip nat outside
 no shutdown

! 4. Маршрут по умолчанию на ISP
ip route 0.0.0.0 0.0.0.0 183.6.9.9

! 5. Статический NAT для серверов
ip nat inside source static 10.9.28.1 183.6.9.10
ip nat inside source static 10.9.28.2 183.6.9.11

! 6. Настройка PAT (Overload) для NET A
access-list 1 permit 192.168.28.0 0.0.0.255
ip nat inside source list 1 interface Gig0/2 overload

exit
copy running-config startup-config
```

---

## 3. Настройка маршрутизатора ISP (Router ISP)

Этот роутер имитирует провайдера. На нем нужно просто поднять интерфейсы.

```ios
enable
configure terminal

! Интерфейс к роутеру NAT
interface Gig0/0
 ip address 183.6.9.9 255.255.255.248
 no shutdown

! Интерфейс к серверу ISP (Сеть D)
interface Gig0/1
 ip address 68.175.255.254 255.240.0.0
 no shutdown

exit
copy running-config startup-config
```

---

## 4. Настройка коммутатора (Switch 1) - Port Security

Зайдите в **CLI** коммутатора:

```ios
enable
configure terminal

! Настройка порта Fa0/1 (PC1) - Sticky + Shutdown
interface FastEthernet0/1
 switchport mode access
 switchport port-security
 switchport port-security mac-address sticky
 switchport port-security violation shutdown

! Настройка порта Fa0/2 (PC2) - Dynamic + Restrict
interface FastEthernet0/2
 switchport mode access
 switchport port-security
 switchport port-security violation restrict

exit
copy running-config startup-config
```

---

## 5. Проверка (Тестирование)

1.  **Проверка NAT:** Пингуйте с **PC1** сервер **ISP (68.160.0.1)**. После этого на роутере NAT введите:
    `show ip nat translations` — Вы должны увидеть запись о трансляции.
2.  **Проверка Port Security:** Попробуйте поменять местами кабели PC1 и PC2.
    *   **Fa0/1** должен выключиться (стать красным).
    *   **Fa0/2** просто перестанет передавать данные, но останется зеленым.

---
**Имя файла для сохранения:** `LR-23-JS-Maxim_Ivanov-Lab8_V3.pkt` 🦾
