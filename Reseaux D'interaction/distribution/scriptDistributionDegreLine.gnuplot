set terminal wxt
set terminal png 10
set encoding utf8
set output "distributionDegLin.png"
set xlabel "k"
set ylabel "p(k)"
set key on inside center top
plot"distributionDeg.txt" with lines lt 2 lw 3
