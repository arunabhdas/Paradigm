# Paradigmatic Agents


## Environment Setup for agent1.py
`
conda create -n paradigmatic-agents python=3.11

conda activate paradigmatic-agents

conda info --envs

pip install pytest-playwright

playwright install firefox

playwright install-deps

python3 agent1.py
`


## Environment Setup for agent2.py

```

python -m venv venv
source venv/bin/activate

pip install --trusted-host pypi.org --trusted-host files.pythonhosted.org numpy==1.24.3
pip install --trusted-host pypi.org --trusted-host files.pythonhosted.org pandas==2.1.4
pip install --trusted-host pypi.org --trusted-host files.pythonhosted.org openpyxl
pip install --trusted-host pypi.org --trusted-host files.pythonhosted.org playwright

playwright install

python3 agent2.py

```


