# Targets marked '# PRIVATE' will be hidden when running `make help`.
# They're helper targets that you probably only need to know about
# if you've got as far as reading the makefile.

# Lists common tasks.
# Also the default task (`make` === `make help`).
help:
	@echo 'No help here... sorry'

watch:
	@echo 'Now watching! 🎃' & \
	sass --watch public/sass:public/stylesheets
