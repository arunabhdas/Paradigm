//
//  ContentView.swift
//
import SwiftUI

struct NavigationAction {
    
}

struct ContentView: View {
    @State private var user: GitHubUser?
    @State private var errorMessage: String?
    var body: some View {
        VStack {
            MapView()
                .frame(height: 300)

            CircleImageView(url: URL(string: user?.avatarUrl ?? ""))
                .offset(y: -130)
                .padding(.bottom, -130)

            VStack(alignment: .leading) {
                Text(user?.name ?? "")
                    .font(.title)

                HStack {
                    Text(user?.bio ?? "")
                    Spacer()
                    Text("California")
                }
                .font(.subheadline)
                .foregroundStyle(.secondary)

                Divider()

                Text("About \(user?.login ?? "")")
                    .font(.title2)
                Text(user?.bio ?? "")
                
            }
            .padding()

            Spacer()
        }
        .task {
            await fetchData()
        }
    }
    private func fetchData() async {
        do {
            let service = GitHubService()
            user = try await service.fetchUserProfile()
        } catch {
            errorMessage = error.localizedDescription
        }
    }
}
#Preview {
    ContentView()
}
