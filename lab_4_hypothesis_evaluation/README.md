# Lab #4: Hypothesis Evaluation

## Objective
Apply various statistical tests (t-tests, z-tests, ANOVA, Chi-Squared) to evaluate research hypotheses and interpret significance levels.

## Implementation
- **Tool:** Python 3.12 and R Programming
- **Tests Performed:**
  - One-Sample & Two-Sample t-tests for mean evaluation.
  - Paired t-tests for dependent samples.
  - z-tests for population means and proportions.
  - ANOVA for multi-group comparison.
  - Chi-Squared for independence testing.

## 🛡️ Защита работы (Как рассказывать преподавателю)

1.  **Цель работы:** "Проверка статистических гипотез на различных наборах данных для принятия обоснованных решений на основе вероятностей."
2.  **Что сделано (Техническая часть):**
    *   "Реализовал 10 различных статистических тестов на двух языках: **Python** (библиотека SciPy) и **R**."
    *   "Для малых выборок использовал **t-тесты Стьюдента**, для больших — **z-тесты**."
    *   "Проверил независимость категориальных признаков (пол и риск-аппетит) с помощью теста **Хи-квадрат Пирсона**."
3.  **Ключевое понятие (P-value):** "В каждой задаче я вычислял уровень значимости $P$. При $P < 0.05$ мы отвергаем нулевую гипотезу и признаем результат статистически значимым."
4.  **Сравнение:** "Например, в задаче с младенцами тест **ANOVA** показал наличие значимых различий в наборе веса в зависимости от типа вскармливания."
