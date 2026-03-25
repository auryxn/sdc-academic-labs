# Настройка коммутаторов (Switch) для Лабораторной работы №8 (Вариант 3)

Сэр, в вашей топологии 3 коммутатора. Основная настройка безопасности (**Port Security**) требуется только на **Switch0** (левый, где компьютеры), так как это указано в задании. На остальных свитчах достаточно просто убедиться, что порты включены.

---

## 1. Настройка Switch0 (Левый, где PC0 и PC1)
Это критическая часть задания. Здесь мы настраиваем **Port Security** согласно Варианту 3.

**Зайдите в CLI Switch0:**
```ios
enable
configure terminal

! 1. Настройка порта Fa0/1 (где PC1) - Sticky + Shutdown
interface FastEthernet0/1
 switchport mode access
 switchport port-security
 switchport port-security mac-address sticky
 switchport port-security violation shutdown

! 2. Настройка порта Fa0/2 (где PC0) - Dynamic + Restrict
interface FastEthernet0/2
 switchport mode access
 switchport port-security
 switchport port-security violation restrict

! 3. Включаем порты
interface range FastEthernet0/1-24
 no shutdown
interface GigabitEthernet0/1-2
 no shutdown

exit
copy running-config startup-config
```

---

## 2. Настройка Switch1 (Центральный, где Серверы)
Здесь **Port Security** обычно не требуется по заданию, нужно просто включить порты, чтобы они стали зелеными.

**Зайдите в CLI Switch1:**
```ios
enable
configure terminal

! Просто переводим все порты в режим доступа и включаем
interface range FastEthernet0/1-24
 switchport mode access
 no shutdown

interface range GigabitEthernet0/1-2
 no shutdown

exit
copy running-config startup-config
```

---

## 3. Настройка Switch2 (Правый, в сети ISP)
Этот свитч соединяет сервер провайдера и компьютер C2. Также просто включаем порты.

**Зайдите в CLI Switch2:**
```ios
enable
configure terminal

interface range FastEthernet0/1-24
 switchport mode access
 no shutdown

exit
copy running-config startup-config
```

---

## ⚠️ Как проверить Port Security (Switch0)
1.  Убедитесь, что PC0 и PC1 пингуют друг друга.
2.  На **Switch0** введите: `show port-security interface fa0/1`. Вы должны увидеть статус **Secure-up**.
3.  **Тест нарушения:** Попробуйте отсоединить кабель от PC1 и воткнуть его в ноутбук или другой компьютер. 
    *   **Fa0/1** должен сразу загореться красным и перейти в режим `err-disabled`.
    *   Чтобы «починить» порт после этого, нужно зайти в него и набрать: `shutdown` затем `no shutdown`.

Удачи, сэр! Теперь свитчи полностью готовы. 🦾
