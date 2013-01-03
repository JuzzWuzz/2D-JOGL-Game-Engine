import os

games =  os.listdir('./games_src')
oss =  os.listdir('./OS_specific_files')

for game in games:
  for opsys in oss:
    os.system('echo "./create_API.sh ' + game + ' ' + opsys+'"')
    os.system('./create_API.sh ' + game + ' ' + opsys)
    print
