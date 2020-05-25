![Image of Yaktocat](https://cdn.app.compendium.com/uploads/user/e7c690e8-6ff9-102a-ac6d-e4aebca50425/e018f109-ab09-4dab-82ba-d3f768c9d795/File/da7fc92c9fefd99848dd99ed73f523c4/unnamed.png)


# kaltura-compatibility-testing-service

Docker
1. gradle build -x test
2. docker build -t springio/kaltura-compatibility-service .
3. docker run -p 8080:8080 springio/kaltura-compatibility-service

Local
1. gradle bootRun