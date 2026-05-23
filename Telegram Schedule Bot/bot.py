import os, json
from schedule import Schedule, week_num
from datetime import datetime, timedelta

CSV_PATH = os.path.join(os.path.dirname(__file__), 'schedule.csv')
GROUP = '25HR-JA'

sched = Schedule(CSV_PATH)

def get_today():
    d = datetime.now()
    slots = sched.get_day(d, GROUP)
    w = week_num(d)
    return f'Today {d.strftime("%a %d-%b")} (W{w}):\n' + sched.format_slots(slots)

def get_tomorrow():
    d = datetime.now() + timedelta(days=1)
    slots = sched.get_day(d, GROUP)
    w = week_num(d)
    return f'Tomorrow {d.strftime("%a %d-%b")} (W{w}):\n' + sched.format_slots(slots)

def get_week():
    d = datetime.now()
    w = week_num(d)
    week_data = sched.get_week(d, GROUP)
    lines = [f'Week {w}:']
    for day, slots in week_data:
        lines.append(f'\n{day.strftime("%a %d-%b")}:')
        lines.append(sched.format_slots(slots))
    return '\n'.join(lines)

if __name__ == '__main__':
    import sys
    cmd = sys.argv[1] if len(sys.argv) > 1 else 'today'
    if cmd == 'today':
        print(get_today())
    elif cmd == 'tomorrow':
        print(get_tomorrow())
    elif cmd == 'week':
        print(get_week())
