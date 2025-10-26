#!/bin/bash

for country_code in ar cl ec mx uy ve; do
  echo "$country_code"
  cp ./common/src/main/resources/assets/spyglass_keybind/lang/{es_es,es_${country_code}}.json
done