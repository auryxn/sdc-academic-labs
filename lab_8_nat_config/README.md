# Лабораторная работа №8: Настройка NAT и Port Security на Cisco

**Группа:** LR-23-JS
**Имя Фамилия:** Максим Иванов (Пример)
**Дата:** 25.03.2026
**Вариант:** 1 (выбран на основе примера в таблице)

---

## 🎯 Цель работы
Освоение принципов трансляции адресов сетевого уровня (NAT/PAT) в маршрутизаторах Cisco и настройка безопасности портов (Port Security) на коммутаторах.

---

## 🛠️ Параметры сети (Вариант 1)

| Сеть | Диапазон / IP |
| :--- | :--- |
| **NET A** | 172.24.26.0/24 |
| **NET B** | 192.168.26.0/24 |
| **Сеть C (Internal Global)** | 18.3.0.0/16 |
| **Сеть D (ISP)** | 153.2.48.0/20 |

### Назначение IP адресов:
*   **Интерфейсы роутеров:** Последний доступный адрес подсети.
*   **Компьютеры/Серверы:** Первый доступный адрес подсети.

---

## 💻 Конфигурация оборудования

### 1. Настройка маршрутизатора NAT (Router NAT)

#### Интерфейсы:
```ios
interface Gig0/0 (inside)
 ip address 172.24.26.254 255.255.255.0
 ip nat inside

interface Gig0/1 (outside)
 ip address 18.3.255.254 255.255.0.0
 ip nat outside

ip route 0.0.0.0 0.0.0.0 18.3.0.1  (Маршрут на ISP)
```

#### Статический NAT (для серверов):
```ios
ip nat inside source static 192.168.26.1 18.3.0.10 (Server 1)
ip nat inside source static 192.168.26.2 18.3.0.11 (Server 2)
```

#### PAT для сети NET A:
```ios
access-list 1 permit 172.24.26.0 0.0.0.255
ip nat inside source list 1 interface Gig0/1 overload
```

### 2. Настройка коммутатора (Switch 1) - Port Security

| Порт | Тип безопасности | Режим нарушения (Violation) |
| :--- | :--- | :--- |
| **Fa0/1 (PC1)** | Static | Protect |
| **Fa0/2 (PC2)** | Sticky | Shutdown |

#### Команды:
```ios
interface Fa0/1
 switchport mode access
 switchport port-security
 switchport port-security violation protect
 switchport port-security mac-address <MAC_PC1>

interface Fa0/2
 switchport mode access
 switchport port-security
 switchport port-security violation shutdown
 switchport port-security mac-address sticky
```

---

## ✅ Проверка работоспособности

1.  **Ping:** Проверка связи между PC1/PC2 и серверами.
2.  **NAT Table:** `show ip nat translations` — проверка активных трансляций.
3.  **Port Security Test:** 
    *   При переподключении PC1 к Fa0/2 порт Fa0/2 должен перейти в статус `err-disabled` (Shutdown), так как MAC-адрес не совпадает со "sticky" адресом PC2.
    *   При переподключении PC2 к Fa0/1 трафик должен блокироваться (Protect) без выключения порта.

---

## 📝 Контрольные вопросы (Кратко)

1.  **Зачем нужен NAT?** Для экономии публичных IPv4 адресов и скрытия внутренней структуры сети.
2.  **Плюсы/Минусы:** Плюсы — безопасность и экономия адресов. Минусы — задержки при трансляции и сложности с некоторыми протоколами (IPsec, FTP).
3.  **PAT vs Static NAT:** Static NAT — 1:1 (постоянный адрес для сервера). PAT — N:1 (много хостов через один IP роутера с использованием портов).

---
**Файл проекта:** `LR-23-JS-Maxim_Ivanov-Lab8.pkt` 🦾
