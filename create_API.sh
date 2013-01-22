# cleaning up dev folder

# create_API GameName OS

mkdir output
rm -rf output/$1"_"$2/
mkdir output/$1"_"$2/

cd common_files/
make all
cd ..

echo Copying to output/$1"_"$2/
cp -r common_files/* output/$1"_"$2/
rm -r output/$1"_"$2/Makefile
cp -r "OS_specific_files/"$2/* output/$1"_"$2/
rm -r output/$1"_"$2/src/
mkdir output/$1"_"$2/src/
cp -r games_src/$1/* output/$1"_"$2/src/

#echo python create_project_files.py "OS_specific_files"$2 $1 output/$1"_"$2/
python create_project_files.py $1 $2 output

