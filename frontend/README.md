# Ionic and Typescript

This is a living repo where the example from the 'Ionic and Typescript' screencast. Here we will go over all the parts to get setup with Ionic V1 and Typescript.


## Part 1 : Setting up a build step.

[![IMAGE ALT TEXT HERE](http://img.youtube.com/vi/kVegt2E72ww/0.jpg)](http://www.youtube.com/watch?v=kVegt2E72ww)

_Link to youtube_

First let's create a project

```bash
$ ionic start myApp tabs
$ cd myApp
$ ionic setup sass
```

We're enabling sass in order to configure our project's gulp build step, as well as the start up tasks (more on that later)

We need to create a `src` directory where we can place all our Typescript files. 
Let's create a `src` directory, then copy all the files in the `www/js/` directory. After that, we'll need to change all the file extensions from `.js` to `.ts`


From here we need to create a build task to compile our code.
Let's use the [gulp-tsc plugin](https://www.npmjs.com/package/gulp-tsc) to compile our Typescript code down to vanilla JavaScript.

Let's install the plugin 

```
$ npm install gulp-tsc --save-dev 
```

Then require that plugin in our gulpfile

```javascript
var typescript = require('gulp-tsc')
```

Now let's add the path to the typescript files to the `paths` variable in this gulpfile

```
var paths = {
  sass: ['./scss/**/*.scss'],
  src: ['./src/*.ts']
};
```


From here, we can create a simple compile task to compile the Typescript down to JavaScript

```
gulp.task('compile', function() {
  gulp.src(paths.src)
    .pipe(typescript({
      emitError: false
    }))
    .pipe(gulp.dest('www/js/'))
})
```

So we'll take the typescript source, pass that into the tsc plugin, and then pipe it back into the `www/js` to finish everything.
Now there's a bit in there for error handling, so when we compile, we'll get some error, but it won't break the compilation. 
These errors are from the compile not knowing things like `angular`, `cordova,` or any Ionic specific features. In part two, we'll fix that.

Now, to finish things off, we'll create a new watch task that will handle any changes in our Typescript source directory.  

```
gulp.task('watch', function() {
  gulp.watch(paths.sass, ['sass']);
  gulp.watch(paths.src, ['compile']);
});
```


## Part 2 : Adding Types to your code

Now that we can compile our code, let's get rid of those errors and start annotating our code.

[![IMAGE ALT TEXT HERE](http://img.youtube.com/vi/H6NxUieUjyM/0.jpg)](http://www.youtube.com/watch?v=H6NxUieUjyM)

There's a big community project, [Definitely Typed](https://github.com/borisyankov/DefinitelyTyped), which adds typings for a large number of frameworks/libraries. To add these typings to our project, we can install a definition manager, [TSD](http://definitelytyped.org/tsd/) from npm. 

```bash
$ npm install -g tsd
```

_Note for linux/osx users, you'll need to add sudo_

With this installed, we can add out typings easily.

```bash
$ tsd install ionic cordova cordova-ionic --save
```

This will install typings for Ionic, Cordova, and the Ionic Keyboard plugin. TSD will also handle any other dependencies that are needed. In this case, Ionic depends on Angular, which also depends on jQuery, so it will install them as well.


From here we can see we have a typings directory with all of our typings we've installed as well as a `tsd.d.ts` file. This way, if you install any more definition files down the road, TSD will update the `tsd.d.ts` file and you can just reference that. Now let's add a reference to that in our Typescript files. For this project, the reference path is 

```
/// <reference path="../typings/tsd.d.ts" />
```

Now that the reference is added at the top of each typescript file we have, we can run `gulp compile` and compile our code without any errors.
With those reference definitions, we can now start to annotate our code and get code completion inside of our editor. 

_Note you want to make sure your editor supports Typescript, a popular, free, cross platform editor is [Visual Studio Code](https://code.visualstudio.com/)_

Let's open our `controllers.ts` and add `$ionicLoading` to the `DashCtrl`

```
.controller('DashCtrl', function($scope, $ionicLoading) {

})
```

We can annotate `$ionicLoading` by adding `: ionic.loading.IonicLoadingService`. This is defined in the `ionic.d.ts` definition file we've installed from TSD.


Now when we type `$ionicLoading`, as soon as we hit the `.`, our editor should give us a list of available methods for `$ionicLoading`. We can do this for any Angular or Cordova code as well. 
