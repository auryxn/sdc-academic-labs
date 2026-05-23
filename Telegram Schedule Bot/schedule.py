import csv, re
from datetime import datetime, timedelta

WEEK1_START = datetime(2026, 3, 2)
SLOT_TIMES = ['9:00-10:30','10:40-12:10','12:20-13:50','14:20-15:50','16:00-17:30','18:00-19:30']

MONTHS = {'Jan':1,'Feb':2,'Mar':3,'Apr':4,'May':5,'Jun':6,
          'Jul':7,'Aug':8,'Sep':9,'Oct':10,'Nov':11,'Dec':12}

def week_num(date):
    diff = (date - WEEK1_START).days
    return max(0, (diff // 7) + 1)

def parse_date(s):
    m = re.match(r'(\d+)-(\w+)', s.strip())
    if not m:
        return None
    day, mon_name = int(m.group(1)), m.group(2)
    mon = MONTHS.get(mon_name, 0)
    if day and mon:
        return datetime(2026, mon, day)

def clean(cell):
    if not cell:
        return []
    return [l.strip() for l in cell.split('\n') if l.strip() and l.strip() not in ('', '�', '\u00a0')]

SLOT_MAP = {4:0, 5:1, 6:2, 8:3, 9:4, 10:5}

class Schedule:
    def __init__(self, csv_path):
        self.data = {}  # (date_iso, group) -> [(slot_idx, [subjects])]
        self._load(csv_path)

    def _load(self, path):
        with open(path, 'r', encoding='utf-8-sig', errors='replace') as f:
            rows = list(csv.reader(f))

        current_date = None

        for row in rows:
            if not row or len(row) < 4:
                continue

            # Check for date in col 1
            d = parse_date(row[1] or '')
            if d:
                current_date = d

            # Check for group in col 3
            gc = (row[3] or '').strip()
            if not gc.startswith('25HR') or current_date is None:
                continue

            items = []
            for col_idx, slot_idx in SLOT_MAP.items():
                if col_idx < len(row):
                    subs = clean(row[col_idx])
                    if subs:
                        items.append((slot_idx, subs))

            if items:
                key = (current_date.isoformat(), gc)
                if key not in self.data:
                    self.data[key] = []
                self.data[key].extend(items)

    def get_day(self, date, group):
        return self.data.get((date.isoformat(), group), [])

    def get_week(self, date, group):
        w = week_num(date)
        res = []
        for (d_iso, g), slots in self.data.items():
            d = datetime.fromisoformat(d_iso)
            if week_num(d) == w and g == group:
                res.append((d, slots))
        return sorted(res, key=lambda x: x[0])

    def format_slots(self, slots):
        if not slots:
            return 'No classes'
        lines = []
        for si, subs in sorted(slots, key=lambda x: x[0]):
            t = SLOT_TIMES[si] if si < len(SLOT_TIMES) else f'Slot {si+1}'
            for s in subs:
                lines.append(f'{t} | {s}')
        return '\n'.join(lines)
