git config --global user.name  'laotitouzi'
git config --global user.email  442748419@qq.com

git remote add org git@github.com:laotitouzi/web.git
git push -u org master
git commit -m 'first commit'   //提交到本地仓库

git fetch org
git merge org/master

git push org master -f   //提交到github远程仓库	