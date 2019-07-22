package ro.ebsv.githubapp.data

/**
 * Used to define the constants and enums of the app.
 */
class Constants {

    class Api {
        companion object {
            const val BASE_URL = "https://api.github.com"
        }
    }

    class SharedPrefsKeys {
        companion object {
            const val USER_PREFS = "USER_PREFS"
            const val USER_NAME = "USER_NAME"
            const val USER_PASS = "USER_PASS"
        }
    }

    class Repository {

        class Filters {

            enum class Visibility {
                all, public, private
            }

            enum class Affiliation {
                owner, collaborator, organization_member
            }

        }

        class Sort {

            enum class Criteria {
                created, updated, pushed, full_name
            }

            enum class Direction {
                asc, desc
            }

        }

        class BundleKeys {
            companion object {
                const val REPOSITORY = "REPOSITORY"
                const val REPO_LIST = "REPO_LIST"
                const val REPO_VISIBILITY = "REPO_VISIBILITY"
                const val DEFAULT_FILTER = "DEFAULT_FILTER"
                const val DEFAULT_SORT = "DEFAULT_SORT"
            }
        }

        companion object {
            const val REQUEST_CODE = 1
        }
    }


}