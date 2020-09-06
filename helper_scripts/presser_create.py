#!python
import sys
import json
from pathlib import Path


def lowercasify_json_element(element):
    if type(element) is dict:
        n_val = lowercasify_json_object(element)
    elif type(element) is list:
        n_val = lowercasify_json_list(element)
    elif type(element) is str:
        n_val = element.lower()
    else:
        n_val = element
    return n_val


def lowercasify_json_list(obj: list):
    return [lowercasify_json_element(ele) for ele in obj]


def lowercasify_json_object(obj: dict):
    n_obj = {}
    for key, val in obj.items():
        n_obj[key.lower()] = lowercasify_json_element(val)
    return n_obj


def print_usage_and_exit():
    print('Usage: ./presser_create.py OPTION FILE')
    print('Options: lowercasify, generate')
    exit(1)


def lowercasify(path):
    with open(path) as load_file:
        j_obj = json.load(load_file)
    print(json.dumps(lowercasify_json_element(j_obj), indent=2, sort_keys=True))


def rename(name: str, mc_names: list):
    if name in mc_names:
        input_name = f'minecraft:{name}'
    else:
        input_name = f'harvestcraft:{name}'
    return input_name


def generate(path: str, output: str):
    Path(output).mkdir(exist_ok=True)
    with open(path) as load_file:
        j_obj: dict = json.load(load_file)
    gen: dict = j_obj['generate']
    mc_names: list = j_obj['minecraft']
    for input_item, outputs in gen.items():
        assert type(input_item) == str
        assert type(outputs) == list
        assert len(outputs) == 2
        recipe = {
            'type': 'harvestcraft:presser',
            'input': {'item': ''},
            'output_one': {'item': ''},
            'output_two': {'item': ''}
        }
        recipe['input']['item'] = rename(input_item, mc_names)
        recipe['output_one']['item'] = rename(outputs[0], mc_names)
        recipe['output_two']['item'] = rename(outputs[1], mc_names)
        with open(f'{output}/{outputs[0]}.json', 'w') as file_out:
            json.dump(recipe, file_out, sort_keys=True, indent=2)


if __name__ == '__main__':
    if len(sys.argv) < 3:
        print_usage_and_exit()
    if sys.argv[1] == 'lowercasify':
        lowercasify(sys.argv[2])
    elif sys.argv[1] == 'generate':
        if len(sys.argv) != 4:
            print_usage_and_exit()
        generate(sys.argv[2], sys.argv[3])
