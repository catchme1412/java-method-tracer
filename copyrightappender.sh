for f in `find -name *.java`
do
  cat copyright.txt $f > tmp; mv tmp $f;
done
