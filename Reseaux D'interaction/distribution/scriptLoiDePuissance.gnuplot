set terminal wxt
set terminal png
set xlabel 'k'
set ylabel 'p(k)'
set output 'coefficients .png'

set logscale xy
set yrange [1e-6:1]

# on va fitter une fonction linéaire en log-log

f(x) = lc - gamma * x
fit f(x) 'distributionDeg.txt' using (log($1)):(log($2)) via lc, gamma

c = exp(lc)
power(k) = c * k ** (-gamma)

plot 'distributionDeg.txt' title 'DBLP', \
  power(x) title 'Power law'



