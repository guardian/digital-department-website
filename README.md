# Guardian Digital Department Website

A digital fellowship scheme project.

A play app to provide information on what it is like to work at Guardian Digital.

* List of recent/future talks by current employees.
* List of open source projects created / incubated by the Guardian.
* List of events run by Guardian Digital.

Once finished it will:

* Replace http://developers.theguardian.com/ and http://guardian.github.io/developers/ and move focus from developers only to wider Digital department.
* Act as a compliment to: https://workforus.theguardian.com/.

# Getting Started

You will need **sass** and **sbt**


If you need to install sass >> http://sass-lang.com/install

If you need to install sbt >> http://www.scala-sbt.org/

### Running the app
1. Run `make watch` to compile the sass, and keep the generated files updated
1. In a separate terminal, run `./scripts/start.sh` to start the app
2. Navigate to [localhost:8460](localhost:8460)


### Adding to the sass
- All new sass files should be added into public/sass
- Use an underscore at the start of a filename to make the sass compiler ignore it (partial file)
- These partial files can be imported into main.scss
- `make watch` will edit or create files into public/stylesheets

N.B. The css files are generated. Any changes to the css files in stylesheets will be overwritten!
