load_lib() {
  local name="$1"
  if ! [ -x "$(command -v brew)" ]; then
    load "/code/test_helper/${name}/load.bash"
  else
    TEST_BREW_PREFIX="$(brew --prefix)"
    load "${TEST_BREW_PREFIX}/lib/${name}/load.bash"
  fi
}

setup() {
    load_lib bats-support
    load_lib bats-assert
    DIR="$( cd "$( dirname "$BATS_TEST_FILENAME" )" >/dev/null 2>&1 && pwd )"
    PATH="$DIR/src:$PATH"
}

@test "Check welcome message" {
    run project.sh
    assert_output --partial 'Welcome to our project!'
}