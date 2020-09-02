import json
import sys as sys

with open(sys.argv[1]) as f:
    obj = json.load(f)
lower_vals = []
for val in obj['values']:
    lower_vals.append(val.lower())
obj['values'] = lower_vals
with open(sys.argv[1], 'w') as f:
    json.dump(obj, f, sort_keys=True, indent=2)

