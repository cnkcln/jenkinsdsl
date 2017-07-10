Jenkins Job DSL / Plugin
========================

The Jenkins "Job DSL / Plugin" is made up of two parts: The Domain Specific Language (DSL) itself that allows users to
describe jobs using a Groovy-based language, and a Jenkins plugin which manages the scripts and the updating of the
Jenkins jobs which are created and maintained as a result.

Background
----------
Jenkins is a wonderful system for managing builds, and people love using its UI to configure jobs.  Unfortunately, as
the number of jobs grows, maintaining them becomes tedious, and the paradigm of using a UI falls apart. Additionally,
the common pattern in this situation is to copy jobs to create new ones, these "children" have a habit of
diverging from their original "template" and consequently it becomes difficult to maintain consistency between these
jobs.

The Jenkins job-dsl-plugin attempts to solve this problem by allowing jobs to be defined with the absolute minimum
effort in a programmatic form.  The goal is for your team to be able to define all the jobs they wish to be related to
their project, declaring their intent for the jobs programmatically, and leaving the common elements in each of them
hidden behind the DSL.


Building
--------
Prerequisites:
* JDK 7 (or above)
* install AnsiColor Plugin
* install Job DSL Plugin
* install Permissive Script Security Plugin
* install Authorize Project Plugin
* install Bitbucket Plugin
* install Build Monitor View Plugin
* install Radiator View Plugin
* install Green Plugin
* install Environment Injector Plugin
* install Flaky Test Handler plugin
* install Test stability history Plugin 


1. Create a Jenkins Job using the Free-style project style to run your DSL Scripts. This is called a "Seed" job
2. Configure the seed job, by adding a "Build Step" of type "Process Job DSLs" and paste in the body of the DSL
3. Run the seed to generate your new jobs from your script. When successful, the "build result" page will list the jobs
which have been successfully created


Author
-------
Ajinkya Kakade
