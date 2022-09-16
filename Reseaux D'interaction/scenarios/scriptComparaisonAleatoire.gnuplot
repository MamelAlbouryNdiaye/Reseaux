set terminal png 10
set encoding utf8
set output "comparaison.png"
set yrange[0:300000]
set xlabel "jours"
set ylabel "Nbr infectés"
set key on inside center top
plot"Sc1Aleatoire.txt" t "Scenario 1" with linesp lt 1 pt 1,"Sc2Aleatoire.txt" t "Scenario 2 "  with linesp lt 2 pt 2,"Sc3Aleatoire.txt" t "Scenario 3  " with linesp lt 3 pt 3
