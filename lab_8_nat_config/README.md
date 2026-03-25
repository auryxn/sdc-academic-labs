# Лабораторная работа №8: Настройка NAT и Port Security на Cisco

**Группа:** LR-23-JS
**Имя Фамилия:** Максим Иванов (Пример)
**Дата:** 25.03.2026
**ВАРИАНТ:** 3 (ВЫБРАН ИЗ ТАБЛИЦЫ)

---

## 🛠️ Параметры сети (Вариант 3)

| Сеть | Диапазон / IP | Маска | Последний доступный (Router) | Первый доступный (Hosts) |
| :--- | :--- | :--- | :--- | :--- |
| **NET A (LAN)** | 192.168.28.0 | /24 (255.255.255.0) | 192.168.28.254 | 192.168.28.1 |
| **NET B (Server)** | 10.9.28.0 | /24 (255.255.255.0) | 10.9.28.254 | 10.9.28.1 |
| **Сеть C (Internal Global)** | 183.6.9.8 | /29 (255.255.255.248) | 183.6.9.14 | 183.6.9.9 |
| **Сеть D (ISP)** | 68.160.0.0 | /12 (255.240.0.0) | 68.175.255.254 | 68.160.0.1 |

---

## 💻 Конфигурация оборудования (CLI)

### 1. Маршрутизатор NAT (Router NAT)

#### Интерфейсы:
```ios
interface Gig0/0 (inside)
 ip address 192.168.28.254 255.255.255.0
 ip nat inside

interface Gig0/1 (inside - servers)
 ip address 10.9.28.254 255.255.255.0
 ip nat inside

interface Gig0/2 (outside)
 ip address 183.6.9.14 255.255.255.248
 ip nat outside

ip route 0.0.0.0 0.0.0.0 183.6.9.9  (Маршрут на ISP)
```

#### Статическая трансляция (NAT) для серверов:
*   **Server 1:** 10.9.28.1 -> 183.6.9.10
*   **Server 2:** 10.9.28.2 -> 183.6.9.11
```ios
ip nat inside source static 10.9.28.1 183.6.9.10
ip nat inside source static 10.9.28.2 183.6.9.11
```

#### Настройка PAT для сети NET A:
```ios
access-list 1 permit 192.168.28.0 0.0.0.255
ip nat inside source list 1 interface Gig0/2 overload
```

### 2. Коммутатор (Switch 1) - Port Security (Вариант 3)

| Порт | Тип безопасности | Режим нарушения (Violation) |
| :--- | :--- | :--- |
| **Fa0/1 (PC1)** | Sticky | Shutdown |
| **Fa0/2 (PC2)** | Dynamic | Restrict |

#### Команды для Switch 1:
```ios
interface Fa0/1 (PC1)
 switchport mode access
 switchport port-security
 switchport port-security mac-address sticky
 switchport port-security violation shutdown

interface Fa0/2 (PC2)
 switchport mode access
 switchport port-security
 switchport port-security violation restrict
```

---

## ✅ Проверка (Команды в Packet Tracer)
1.  **Проверка NAT:** `show ip nat translations`
2.  **Проверка Port Security:** `show port-security interface Fa0/1` и `show port-security interface Fa0/2`.
3.  **Тест безопасности:** Переподключите PC1 к Fa0/2. Порт Fa0/1 должен перейти в статус `err-disabled` (красный индикатор), так как MAC "прилип" (sticky) к нему.

---

## 📝 Контрольные вопросы
1.  **Зачем NAT?** Для сохранения ограниченного пула публичных IPv4-адресов и обеспечения базового уровня безопасности.
2.  **PAT vs Static NAT:** Static NAT резервирует 1 публичный IP за 1 внутренним (обычно для серверов). PAT (Overload) позволяет всей локальной сети выходить в интернет через 1 общий IP маршрутизатора.
3.  **Порт Секьюрити:** Предотвращает несанкционированное подключение сторонних устройств к сети предприятия.

---
**Имя файла:** `LR-23-JS-Maxim_Ivanov-Lab8_V3.pkt` 🦾
