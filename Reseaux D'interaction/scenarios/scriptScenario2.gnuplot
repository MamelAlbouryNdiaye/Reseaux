set terminal png
set encoding utf8
set yrange [0:300000]
set output "scen2.png"
set xlabel "Jours"
set ylabel "Nbr infectés"
set key on inside center top
plot "sc2.txt" with lines lt 2 lw 3

