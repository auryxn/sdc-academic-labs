# Пошаговая настройка коммутаторов (Switch) в CLI

Сэр, вот конкретные действия для каждого коммутатора. Просто заходите на вкладку **CLI** каждого устройства и вставляйте эти команды.

---

### **1. Настройка Switch0 (Левый, к ПК)**
Этот свитч отвечает за **Port Security** (Вариант 3).

**Команды для CLI:**
```ios
enable
configure terminal

! Заходим в первый порт (для PC1)
interface FastEthernet0/1
 switchport mode access
 switchport port-security
 switchport port-security mac-address sticky
 switchport port-security violation shutdown

! Заходим во второй порт (для PC0)
interface FastEthernet0/2
 switchport mode access
 switchport port-security
 switchport port-security violation restrict

! Включаем все порты (на всякий случай)
interface range FastEthernet0/1-24
 no shutdown
exit
write
```

---

### **2. Настройка Switch1 (Центральный, к серверам)**
Здесь просто переводим порты в режим доступа и включаем их.

**Команды для CLI:**
```ios
enable
configure terminal

! Настраиваем все 24 порта сразу
interface range FastEthernet0/1-24
 switchport mode access
 no shutdown

! Настраиваем гигабитные порты (к роутеру)
interface range GigabitEthernet0/1-2
 no shutdown
exit
write
```

---

### **3. Настройка Switch2 (Правый, в сети ISP)**
Аналогично Switch1 — просто включаем порты.

**Команды для CLI:**
```ios
enable
configure terminal

! Настраиваем все порты
interface range FastEthernet0/1-24
 switchport mode access
 no shutdown

interface range GigabitEthernet0/1-2
 no shutdown
exit
write
```

---

## 🛠️ Что эти команды значат (для защиты):
1.  `switchport mode access`: Переводит порт в режим подключения конечного устройства (ПК/Сервер), а не другого свитча/транка.
2.  `switchport port-security`: Активирует функцию безопасности на порту.
3.  `mac-address sticky`: Свитч «запоминает» MAC-адрес первого подключенного устройства и записывает его в конфиг.
4.  `violation shutdown`: Если подключить другое устройство, порт выключится (загорится красным).
5.  `violation restrict`: Если подключить другое устройство, данные не пройдут, но порт останется зеленым.
6.  `no shutdown`: Команда «включить», чтобы индикатор стал зеленым.

Удачи, сэр! Всё запушено в ваш GitHub. 🦾
