set terminal png
set title "Scénario 1 : On ne fait rien pour empêcher l'épidémie"
set encoding utf8
set output "scen1.png"
set xlabel "Jours"
set ylabel "Nombre d’infectés"
set key on inside center top
plot "scenarioOne.dat" with lines lt 2 lw 3

